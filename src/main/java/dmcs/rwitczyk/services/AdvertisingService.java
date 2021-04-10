package dmcs.rwitczyk.services;

import dmcs.rwitczyk.domains.AdvertisingGroupEntity;
import dmcs.rwitczyk.repository.AdvertisingGroupRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class AdvertisingService {

    @Autowired
    private AdvertisingGroupRepository advertisingGroupRepository;


    public List<String> getPatientAdvertisingGroups(int patientId) {
        List<AdvertisingGroupEntity> advertisingGroupEntities = advertisingGroupRepository.findByPatientId(patientId);
        List<String> advertisingGroups = new ArrayList<>();

        advertisingGroupEntities.stream().forEach(group -> advertisingGroups.add(group.getName()));
        return advertisingGroups;
    }


}
