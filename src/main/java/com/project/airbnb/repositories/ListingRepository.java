package com.project.airbnb.repositories;

import com.project.airbnb.dtos.response.ListingResponse;
import com.project.airbnb.models.Listing;
import com.project.airbnb.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface ListingRepository extends JpaRepository<Listing, String> , JpaSpecificationExecutor<Listing> {
    Page<Listing> findByHost(User user, Pageable pageable);

    @Query(value = """
    with destination as (
        select st_transform(st_setsrid(st_makepoint(:longitude, :latitude), 4326), 3857) as geom
    )
    select id, listing_name, description, images, bedrooms, vh.night_amount, vh.total_amount, address, longitude, latitude
    from destination d, homestay hs inner join
    (
        select h.id, avg(ha.price) as night_amount, sum(ha.price) as total_amount
         from destination d,
              homestay h
         join homestay_availability ha on h.id = ha.homestay_id
         where st_dwithin(h.geom, d.geom, :radius)
           and h.guests >= :guests
           and ha.date between :checkinDate and :checkoutDate
           and ha.status = :status
         group by h.id
         having count(ha.date) = :nights
    ) as vh using (id)
    order by hs.geom <-> d.geom
    """, nativeQuery = true)
    List<ListingResponse> searchHomestay(@Param("longitude") Double longitude,
                                         @Param("latitude") Double latitude,
                                         @Param("radius") Double radius,
                                         @Param("checkinDate") LocalDate checkinDate,
                                         @Param("checkoutDate") LocalDate checkoutDate,
                                         @Param("nights") Integer nights,
                                         @Param("guests") Integer guests,
                                         @Param("status") Integer status);
}
