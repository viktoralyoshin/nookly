package com.nookly.booking.reservation.room_block.service;

import com.nookly.booking.accommodation.amenity.dto.AmenityMapper;
import com.nookly.booking.accommodation.category.dto.ResponseCategoryOnlyDTO;
import com.nookly.booking.accommodation.category.model.Category;
import com.nookly.booking.accommodation.category.repository.ICategoryRepository;
import com.nookly.booking.exception.DataNotFoundException;
import com.nookly.booking.hotel.dto.HotelResponseDTO;
import com.nookly.booking.hotel.model.Hotel;
import com.nookly.booking.hotel.repository.IHotelRepository;
import com.nookly.booking.pricing.tariff.dto.TariffMapper;
import com.nookly.booking.reservation.room.dto.RoomMapper;
import com.nookly.booking.reservation.room.model.Room;
import com.nookly.booking.reservation.room.repository.IRoomRepository;
import com.nookly.booking.reservation.room_block.dto.BlockDayDTO;
import com.nookly.booking.reservation.room_block.dto.BlockPeriodDTO;
import com.nookly.booking.reservation.room_block.dto.SearchDTO;
import com.nookly.booking.reservation.room_block.model.RoomBlock;
import com.nookly.booking.reservation.room_block.repository.IRoomBlockRepository;
import com.nookly.booking.yandex.GeoCoderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoomAvailabilityService implements IRoomAvailabilityService {
    private final ICategoryRepository categoryRepository;
    private final IRoomRepository roomRepository;
    private final IRoomBlockRepository roomBlockRepository;
    private final RoomMapper roomMapper;
    private final AmenityMapper amenityMapper;
    private final TariffMapper tariffMapper;
    private final IHotelRepository hotelRepository;
    private final GeoCoderService geoCoderService;
    private final Logger log = LogManager.getLogger(RoomAvailabilityService.class);

    public RoomAvailabilityService(ICategoryRepository categoryRepository, IRoomRepository roomRepository, IRoomBlockRepository roomBlockRepository, RoomMapper roomMapper, AmenityMapper amenityMapper, TariffMapper tariffMapper, IHotelRepository iHotelRepository, GeoCoderService geoCoderService) {
        this.categoryRepository = categoryRepository;
        this.roomRepository = roomRepository;
        this.roomBlockRepository = roomBlockRepository;
        this.roomMapper = roomMapper;
        this.amenityMapper = amenityMapper;
        this.tariffMapper = tariffMapper;
        this.hotelRepository = iHotelRepository;
        this.geoCoderService = geoCoderService;
    }

    private Boolean isRoomAvailable(UUID roomId, LocalDate checkIn, LocalDate checkOut) {
        List<RoomBlock> blocks = roomBlockRepository.findByRoomIdAndBlockDateBetween(roomId, checkIn, checkOut);
        return blocks.isEmpty();
    }

    private List<Hotel> getHotels(Double latitude, Double longitude) {
        GeoCoderService.BoundingBox bb = geoCoderService.getCityBounds(
                latitude,
                longitude
        );

        log.info("Searching hotels in bounding box: minLat={}, maxLat={}, minLon={}, maxLon={}",
                bb.getMinLat(), bb.getMaxLat(), bb.getMinLon(), bb.getMaxLon());

        return hotelRepository.findHotelsInBoundingBox(
                bb.getMinLat(),
                bb.getMaxLat(),
                bb.getMinLon(),
                bb.getMaxLon()
        );
    }

    @Override
    @Transactional
    public void blockRoomForPeriod(UUID roomID, BlockPeriodDTO blockPeriodDTO) {
        Room room = roomRepository.findById(roomID).orElseThrow(() -> new DataNotFoundException("Room not found"));

        List<LocalDate> datesToBlock = blockPeriodDTO.getStartDate().datesUntil(blockPeriodDTO.getEndDate().plusDays(1)).toList();

        List<RoomBlock> existingBlocks = roomBlockRepository.findByRoomIdAndBlockDateBetween(roomID, blockPeriodDTO.getStartDate(), blockPeriodDTO.getEndDate());

        roomBlockRepository.deleteAll(existingBlocks);

        List<RoomBlock> newBlocks = datesToBlock.stream()
                .map(date -> new RoomBlock(
                        null,
                        room,
                        date,
                        blockPeriodDTO.getBlockReason(),
                        LocalDateTime.now(),
                        LocalDateTime.now()))
                .toList();
        roomBlockRepository.saveAll(newBlocks);
    }

    @Override
    @Transactional
    public void blockRoomForDay(UUID roomID, BlockDayDTO blockDayDTO) {
        Room room = roomRepository.findById(roomID).orElseThrow(() -> new DataNotFoundException("Room not found"));

        boolean alreadyBlocked = roomBlockRepository.existsByRoomIdAndBlockDate(roomID, blockDayDTO.getDate());
        if (!alreadyBlocked) {
            RoomBlock block = new RoomBlock(
                    null,
                    room,
                    blockDayDTO.getDate(),
                    blockDayDTO.getBlockReason(),
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            roomBlockRepository.save(block);
        }
    }

    @Override
    @Transactional
    public void unblockRoomForPeriod(UUID roomID, BlockPeriodDTO blockPeriodDTO) {
        List<RoomBlock> blocks = roomBlockRepository.findByRoomIdAndBlockDateBetween(roomID, blockPeriodDTO.getStartDate(), blockPeriodDTO.getEndDate());
        roomBlockRepository.deleteAll(blocks);
    }

    @Override
    @Transactional
    public void unblockRoomForDay(UUID roomID, BlockDayDTO blockDayDTO) {
        RoomBlock block = roomBlockRepository.findByRoomIdAndBlockDate(roomID, blockDayDTO.getDate());
        roomBlockRepository.delete(block);
    }

    @Override
    @Transactional
    public List<ResponseCategoryOnlyDTO> getAvailableCategories(
            String hotelNumber,
            LocalDate checkIn,
            LocalDate checkOut,
            Integer capacity
    ) {

        Hotel hotel = hotelRepository.findByHotelNumber(hotelNumber).orElseThrow(() -> new DataNotFoundException("Hotel not found"));

        List<Category> categories = categoryRepository.findByHotelIdAndCapacity(hotel.getId(), capacity);

        return categories.stream().map(category -> {
            List<Room> rooms = roomRepository.findByCategoryId(category.getId());

            List<Room> availableRooms = rooms.stream().
                    filter(room -> isRoomAvailable(room.getId(), checkIn, checkOut))
                    .toList();

            return getResponseCategoryOnlyDTO(category, availableRooms);
        }).filter(dto -> !dto.getRooms().isEmpty()).sorted(Comparator.comparing(ResponseCategoryOnlyDTO::getPrice)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<HotelResponseDTO> getAvailableHotels(SearchDTO search) {
        List<Hotel> hotels = getHotels(search.getLatitude(), search.getLongitude());

        return hotels.stream().map(
                hotel -> {
                    HotelResponseDTO hotelResponseDTO = new HotelResponseDTO();
                    hotelResponseDTO.setId(hotel.getId());
                    hotelResponseDTO.setName(hotel.getName());
                    hotelResponseDTO.setHotelNumber(hotel.getHotelNumber());
                    hotelResponseDTO.setStarRating(hotel.getStarRating());
                    hotelResponseDTO.setDescription(hotel.getDescription());
                    hotelResponseDTO.setEmail(hotel.getEmail());
                    hotelResponseDTO.setLocation(hotel.getLocation());
                    hotelResponseDTO.setLongitude(hotel.getLongitude());
                    hotelResponseDTO.setLatitude(hotel.getLatitude());
                    hotelResponseDTO.setStatus(hotel.getStatus());
                    hotelResponseDTO.setPhone(hotel.getPhone());
                    hotelResponseDTO.setRating(hotel.getRating());
                    hotelResponseDTO.setCreatedAt(hotel.getCreatedAt());
                    hotelResponseDTO.setUpdatedAt(hotel.getUpdatedAt());

                    List<Category> categories = categoryRepository.findByHotelIdAndCapacity(hotel.getId(), search.getCapacity());

                    List<ResponseCategoryOnlyDTO> categoryDTOs = categories.stream().map(
                            category -> {
                                List<Room> rooms = roomRepository.findByCategoryId(category.getId());

                                List<Room> availableRooms = rooms.stream().
                                        filter(room -> isRoomAvailable(room.getId(), search.getCheckIn(), search.getCheckOut()))
                                        .toList();

                                if (!availableRooms.isEmpty()) {
                                    ResponseCategoryOnlyDTO categoryDTO = getResponseCategoryOnlyDTO(category, availableRooms);
                                    return categoryDTO;
                                }
                                return null;
                            }
                    ).filter(Objects::nonNull).sorted(Comparator.comparing(ResponseCategoryOnlyDTO::getPrice)).toList();

                    hotelResponseDTO.setCategories(categoryDTOs);
                    return hotelResponseDTO;
                }
        ).filter(hotelResponseDTO -> !hotelResponseDTO.getCategories().isEmpty()).collect(Collectors.toList());
    }

    private ResponseCategoryOnlyDTO getResponseCategoryOnlyDTO(Category category, List<Room> availableRooms) {
        ResponseCategoryOnlyDTO dto = new ResponseCategoryOnlyDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCapacity(category.getCapacity());
        dto.setRooms(availableRooms.stream().map(roomMapper::toResponseRoomOnlyDTO).collect(Collectors.toList()));
        dto.setAmenities(category.getAmenities().stream().map(amenityMapper::toResponseAmenityOnlyDTO).collect(Collectors.toList()));
        dto.setPrice(category.getPrice());
        dto.setTariffs(category.getTariffs().stream().map(tariffMapper::toResponseTariffOnlyDTO).collect(Collectors.toList()));
        return dto;
    }
}
