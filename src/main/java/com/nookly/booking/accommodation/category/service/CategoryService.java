package com.nookly.booking.accommodation.category.service;

import com.nookly.booking.accommodation.amenity.model.Amenity;
import com.nookly.booking.accommodation.amenity.repository.IAmenityRepository;
import com.nookly.booking.accommodation.category.dto.CategoryMapper;
import com.nookly.booking.accommodation.category.dto.RequestCategoryDTO;
import com.nookly.booking.accommodation.category.dto.ResponseCategoryDTO;
import com.nookly.booking.accommodation.category.dto.UpdateCategoryDTO;
import com.nookly.booking.accommodation.category.model.Category;
import com.nookly.booking.accommodation.category.repository.ICategoryRepository;
import com.nookly.booking.exception.DataNotFoundException;
import com.nookly.booking.hotel.model.Hotel;
import com.nookly.booking.hotel.repository.IHotelRepository;
import com.nookly.booking.pricing.tariff.repository.ITariffRepository;
import com.nookly.booking.reservation.room.model.Room;
import com.nookly.booking.reservation.room.repository.IRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
class CategoryService implements ICategoryService {
    private final ICategoryRepository categoryRepository;
    private final IRoomRepository roomRepository;
    private final IAmenityRepository amenityRepository;
    private final CategoryMapper categoryMapper;
    private final IHotelRepository hotelRepository;
    private final ITariffRepository tariffRepository;

    CategoryService(ICategoryRepository categoryRepository,
                    IRoomRepository roomRepository,
                    IAmenityRepository amenityRepository,
                    CategoryMapper categoryMapper,
                    IHotelRepository hotelRepository,
                    ITariffRepository tariffRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.roomRepository = roomRepository;
        this.amenityRepository = amenityRepository;
        this.categoryMapper = categoryMapper;
        this.hotelRepository = hotelRepository;
        this.tariffRepository = tariffRepository;
    }

    @Override
    public ResponseCategoryDTO getCategoryById(UUID id) {
        return categoryRepository.findById(id).map(categoryMapper::toResponseCategoryDTO)
                .orElseThrow(() -> new DataNotFoundException("Category not found"));
    }

    @Override
    public List<ResponseCategoryDTO> getCategories() {
        return categoryRepository.findAll().stream().map(categoryMapper::toResponseCategoryDTO).toList();
    }

    @Override
    @Transactional
    public ResponseCategoryDTO createCategory(RequestCategoryDTO requestCategoryDTO) {

        Category category = categoryMapper.requestToCategory(requestCategoryDTO);

        Hotel hotel = hotelRepository.findByHotelNumber(requestCategoryDTO.getHotelNumber()).orElseThrow(() -> new DataNotFoundException("Hotel not found"));
        category.setHotel(hotel);

        List<Amenity> amenities = amenityRepository.findAllById(requestCategoryDTO.getAmenityIds());
        category.setAmenities(amenities);

        categoryRepository.save(category);

        List<Room> rooms = requestCategoryDTO.getRoomNumbers().stream()
                .map(number -> {
                    Room room = new Room();
                    room.setRoomNumber(number);
                    room.setCategory(category);
                    return room;
                })
                .collect(Collectors.toList());

        roomRepository.saveAll(rooms);
        category.setRooms(rooms);

        return categoryMapper.toResponseCategoryDTO(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public ResponseCategoryDTO updateCategory(UUID id, UpdateCategoryDTO updateCategoryDTO) {

        Category category = categoryRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Category not found"));

        if (updateCategoryDTO.getName() != null) category.setName(updateCategoryDTO.getName());
        if (updateCategoryDTO.getPrice() != null) category.setPrice(updateCategoryDTO.getPrice());
        if (updateCategoryDTO.getCapacity() != null) category.setCapacity(updateCategoryDTO.getCapacity());
        if (updateCategoryDTO.getAmenityIds() != null) {
            List<Amenity> amenities = amenityRepository.findAllById(updateCategoryDTO.getAmenityIds());
            category.setAmenities(amenities);
        }
        if (updateCategoryDTO.getRoomNumbers() != null) {
            Set<Integer> existingRoomNumbers = category.getRooms().stream()
                    .map(Room::getRoomNumber)
                    .collect(Collectors.toSet());

            Set<Integer> newRoomNumbers = new HashSet<>(updateCategoryDTO.getRoomNumbers());

            Set<Integer> roomsToDelete = new HashSet<>(existingRoomNumbers);
            roomsToDelete.removeAll(newRoomNumbers);

            Set<Integer> roomsToAdd = new HashSet<>(existingRoomNumbers);
            roomsToAdd.removeAll(newRoomNumbers);

            List<Room> roomsToRemove = category.getRooms().stream()
                    .filter(room -> roomsToDelete.contains(room.getRoomNumber()))
                    .collect(Collectors.toList());
            roomRepository.deleteAll(roomsToRemove);

            List<Room> roomsToCreate = roomsToAdd.stream()
                    .map(number -> {
                        Room room = new Room();
                        room.setRoomNumber(number);
                        room.setCategory(category);
                        return room;
                    })
                    .collect(Collectors.toList());
            roomRepository.saveAll(roomsToCreate);
        }

        categoryRepository.save(category);

        return categoryMapper.toResponseCategoryDTO(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        categoryRepository.deleteAmenityRelations(id);
        categoryRepository.deleteRooms(id);
        categoryRepository.deleteTariffRelations(id);

        categoryRepository.deleteById(id);
    }
}
