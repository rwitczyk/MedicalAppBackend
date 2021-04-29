package dmcs.rwitczyk.controller;

import dmcs.rwitczyk.services.AdvertisingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/advertising")
@CrossOrigin(origins = "http://localhost:4200")
public class AdvertisingController {

    private AdvertisingService advertisingService;

    @Autowired
    public AdvertisingController(AdvertisingService advertisingService) {
        this.advertisingService = advertisingService;
    }


    @GetMapping(value = "/patient/{accountId}")
    public ResponseEntity getPatientAdvertisingGroups(@PathVariable int accountId) {
        return new ResponseEntity<>(advertisingService.getPatientAdvertisingGroups(accountId), HttpStatus.OK);
    }

}
