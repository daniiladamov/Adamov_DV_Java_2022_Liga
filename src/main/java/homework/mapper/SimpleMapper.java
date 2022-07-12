package homework.mapper;

import homework.exception.MappingException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface SimpleMapper<T> {
    String mapToString(T entity);

    T mapToEntity(String line) throws MappingException;

    default List<String> mapToStringList(Collection<T> entities) {
        List<String> resultList = new ArrayList<>();
        for (T entity : entities) {
            String r = this.mapToString(entity);
            resultList.add(r);
        }
        return resultList;
    }

    default List<T> mapToEntityList(List<String> lines) {
        List<T> resultList = new ArrayList<>();
        for (String line : lines) {
            try {
                T r = this.mapToEntity(line);
                resultList.add(r);
            } catch (MappingException e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }


}
