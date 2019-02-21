package ua.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ControllerUtils {

    public static String buildParams(boolean hasContent, Pageable pageable, String getSearch) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("?page=");
        if (!hasContent)
            buffer.append(pageable.getPageNumber());
        else {
            buffer.append((pageable.getPageNumber() + 1));
        }
        buffer.append("&size=");
        buffer.append(pageable.getPageSize());
        if (pageable.getSort() != null) {
            buffer.append("&sort=");
            Sort sort = pageable.getSort();
            sort.forEach(order -> {
                buffer.append(order.getProperty());
                if (order.getDirection() != Sort.Direction.ASC)
                    buffer.append(",desc");
            });
        }
        if (!getSearch.isEmpty()) {
            buffer.append("&search=");
            buffer.append(getSearch);
        }
        return buffer.toString();
    }
}
