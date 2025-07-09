package com.sharehome.place.query;

import static com.sharehome.place.domain.QPlace.place;
import static com.sharehome.place.domain.QUnavailableDate.unavailableDate;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sharehome.place.query.dao.PlaceSearchCondition;
import com.sharehome.place.query.dao.PlacesSearchDao;
import com.sharehome.place.query.dao.QPlacesSearchDto;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

@Component
public class PlaceSearchQuery {

    private final JPAQueryFactory queryFactory;

    public PlaceSearchQuery(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<PlacesSearchDao> searchPlaces(PlaceSearchCondition condition) {
        return queryFactory
                .select(new QPlacesSearchDto(
                        place.id,
                        place.name,
                        place.bedCount,
                        place.address,
                        place.weekdayPrice,
                        place.weekendPrice
                ))
                .from(place)
                .where(
                        nameContains(condition.name()),
                        guestCountGoe(condition.guestCount()),
                        cityEq(condition.city())
                )
                .fetch();
    }

    public Page<PlacesSearchDao> searchPlacesPage(PlaceSearchCondition condition, Pageable pageable) {

        List<PlacesSearchDao> content = queryFactory
                .select(new QPlacesSearchDto(
                        place.id,
                        place.name,
                        place.bedCount,
                        place.address,
                        place.weekdayPrice,
                        place.weekendPrice
                ))
                .from(place)
                .leftJoin(place.unavailableDates, unavailableDate)
                .where(
                        nameContains(condition.name()),
                        guestCountGoe(condition.guestCount()),
                        cityEq(condition.city()),
                        isValidDate(condition.checkInDate(), condition.checkOutDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(place.count())
                .from(place)
                .where(
                        nameContains(condition.name()),
                        guestCountGoe(condition.guestCount()),
                        cityEq(condition.city()),
                        isValidDate(condition.checkInDate(), condition.checkOutDate())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private static BooleanExpression nameContains(String nameCond) {
        return nameCond != null ? place.name.containsIgnoreCase(nameCond) : null;
    }

    private static BooleanExpression guestCountGoe(Integer guestCountCond) {
        return guestCountCond != null ? place.maxGuestCount.goe(guestCountCond) : null;
    }

    private static BooleanExpression cityEq(String cityCond) {
        return cityCond != null ? place.address.city.eq(cityCond) : null;
    }

    private static BooleanExpression isValidDate(LocalDate checkInDateCond, LocalDate checkOutDateCond) {
        if (checkInDateCond != null && checkOutDateCond != null) {
            return place.unavailableDates.any().date.notBetween(checkInDateCond, checkOutDateCond);
        }
        return null;
    }
}
