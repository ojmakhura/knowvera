package bw.co.centralkyc.organisation;

import java.util.Collection;
import java.util.Map;

import org.mapstruct.Mapper;

import bw.co.centralkyc.utils.MappingUtils;

@Mapper(componentModel = "spring", uses = {
    MappingUtils.class
})
public interface OrganisationDomainMapper {

    default Map toMap(OrganisationDomain domain) {
        if (domain == null)
            return null;
        return Map.of(
                "name", domain.getName(),
                "verified", domain.getVerified());
    }

    default OrganisationDomain toOrganisationDomain(Map map) {
        if (map == null)
            return null;
        return new OrganisationDomain(
                (String) map.get("name"),
                (Boolean) map.get("verified"));
    }

    default Collection<OrganisationDomain> toOrganisationDomainCollection(Collection<Map> maps) {
        if (maps == null)
            return null;
        return maps.stream()
                .map(m -> this.toOrganisationDomain(m))
                .toList();
    }

    default Collection<Map> toMapCollection(Collection<OrganisationDomain> domains) {
        if (domains == null)
            return null;
        return domains.stream()
                .map(d -> this.toMap(d))
                .toList();
    }
}
