package cs.vsu.otamapper.mapper;

import java.util.List;

public interface OTAMapper {

    List<MappedParameter> map(List<String> data);
}
