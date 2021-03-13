package dmcs.rwitczyk.utils;

import dmcs.rwitczyk.domains.OneVisitEntity;
import dmcs.rwitczyk.models.VisitStatusEnum;
import dmcs.rwitczyk.repository.OneVisitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CancelVisitScheduler {

    private OneVisitRepository oneVisitRepository;

    @Autowired
    public CancelVisitScheduler(OneVisitRepository oneVisitRepository) {
        this.oneVisitRepository = oneVisitRepository;
    }

    //    @Scheduled(fixedDelay = 10000, initialDelay = 10000)
    public void execute() {
        log.info("CancelVisitScheduler - anulowanie wszystkich wizyt");
        List<OneVisitEntity> oneVisitEntities = oneVisitRepository.findAll();

        for (OneVisitEntity oneVisitEntity : oneVisitEntities) {
            if (oneVisitEntity.getStatus().equals(VisitStatusEnum.TO_ACCEPT)) {
                oneVisitEntity.setStatus(VisitStatusEnum.CANCELLED);
                oneVisitRepository.save(oneVisitEntity);
                log.info("CancelVisitScheduler - anuluje wizyte o id:" + oneVisitEntity.getId());
            }
        }
    }
}
