package com.project.airbnb.repositories;

import com.project.airbnb.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, String> {
}
