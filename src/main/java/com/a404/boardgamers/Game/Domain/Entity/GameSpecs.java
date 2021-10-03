package com.a404.boardgamers.Game.Domain.Entity;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameSpecs {
    public static Specification<Game> searchWith(Map<SearchKey, Object> searchKeyword) {
        return (Specification<Game>) ((root, query, builder) -> {
            List<Predicate> predicate = getPredicateWithKeyword(searchKeyword, root, builder);
            return builder.and(predicate.toArray(new Predicate[0]));
        }
        );
    }

    private static List<Predicate> getPredicateWithKeyword(Map<SearchKey, Object> searchKeyword, Root<Game> root, CriteriaBuilder builder) {
        List<Predicate> predicate = new ArrayList<>();
        for (SearchKey key : searchKeyword.keySet()) {
            switch (key) {
                case NAME:
                case NAMEKOR:
                case CATEGORY:
                    predicate.add(builder.like(
                            root.get(key.value), "%" + searchKeyword.get(key) + "%"
                    ));
                    break;
                case MINAGE:
                case MINPLAYERS:
                    predicate.add(builder.greaterThanOrEqualTo(
                            root.get(key.value), Integer.valueOf(searchKeyword.get(key).toString())
                    ));
                    break;
                case MAXPLAYERS:
                case MAXPLAYTIME:
                    predicate.add(builder.lessThanOrEqualTo(
                            root.get(key.value), Integer.valueOf(searchKeyword.get(key).toString())
                    ));
                    break;
                default:
                    break;
            }
        }
        return predicate;
    }


    public enum SearchKey {
        NAME("name"),
        NAMEKOR("nameKor"),
        CATEGORY("category"),
        MINAGE("minAge"),
        MINPLAYERS("minPlayers"),
        MAXPLAYERS("maxPlayers"),
        MAXPLAYTIME("maxPlayTime");

        private final String value;

        SearchKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
