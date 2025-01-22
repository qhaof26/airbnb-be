package com.project.airbnb.repositories;

import com.project.airbnb.dtos.response.ListingDTO;
import com.project.airbnb.models.Listing;
import com.project.airbnb.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;


public interface ListingRepository extends JpaRepository<Listing, String> , JpaSpecificationExecutor<Listing> {
    Page<Listing> findByHost(User user, Pageable pageable);

    @Query(value = """
        with destination as (
                select st_setsrid(st_makepoint(:longitude, :latitude), 4326) as geom
            )
        select id, listing_name, nightly_price, address, status, images
        from destination d, listings hs
        inner join(select h.id from destination d, listings h
                        join listing_availabilities la on h.id = la.listing_id
                                where st_dwithin(h.geom, d.geom, :radius)
        and h.num_guests >= :guests
        and la.date between :checkinDate and :checkoutDate
        and la.status = 0
        group by h.id
        having count(la.date) = :nights
                    ) as vh using (id)
        order by hs.geom <-> d.geom
        """, nativeQuery = true)
    Page<ListingDTO> searchListing(@Param("longitude") Double longitude,
                                   @Param("latitude") Double latitude,
                                   @Param("radius") Double radius,
                                   @Param("checkinDate") LocalDate checkinDate,
                                   @Param("checkoutDate") LocalDate checkoutDate,
                                   @Param("nights") Integer nights,
                                   @Param("guests") Integer guests,
                                   Pageable pageable);
}


