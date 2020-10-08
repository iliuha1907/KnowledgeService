package com.senla.training.hoteladmin.dto.mapper;

import com.senla.training.hoteladmin.dto.RoomDto;
import com.senla.training.hoteladmin.model.room.Room;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoomMapper {

    @Autowired
    private ModelMapper mapper;

    public Room toEntity(RoomDto dto) {
        if (dto == null) {
            return null;
        }
        return mapper.map(dto, Room.class);
    }

    public RoomDto toDto(Room entity) {
        if (entity == null) {
            return null;
        }
        return mapper.map(entity, RoomDto.class);
    }

    public List<RoomDto> listToDto(List<Room> rooms) {
        List<RoomDto> roomDtos = new ArrayList<>();
        rooms.forEach(room -> {
            if (room != null) {
                roomDtos.add(mapper.map(room, RoomDto.class));
            }
        });
        return roomDtos;
    }
}
