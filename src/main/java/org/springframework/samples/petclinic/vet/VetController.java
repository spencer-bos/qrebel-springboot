/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.vet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
class VetController {

    private static final String RADIOLOGY = "radiology";
    private static final String SURGERY = "surgery";
    private static final String DENTISTRY = "dentistry";
    private final VetRepository vets;

    public VetController(VetRepository clinicService) {
        this.vets = clinicService;
    }

    @GetMapping("/vets.html")
    public String showVetList(Map<String, Object> model) {
        // Here we are returning an object of type 'Vets' rather than a collection of Vet
        // objects so it is simpler for Object-Xml mapping
        Vets vets = new Vets();

        // 1. here we get full collection of Vets from DB for the first time
        vets.getVetList().addAll(this.vets.findAll());

        // 2. now we want to perform some analysis of our Vets
        Map<String, Integer> specialityStats = getSpecialityStats(vets);

        model.put("totalVets", vets.getVetList().size());
        model.put("vetsSpecs", specialityStats);
        model.put("vets", vets);
        return "vets/vetList";
    }

    private Map<String, Integer> getSpecialityStats(Vets vets) {
        Map<String, Integer> specialityStats = new HashMap<>();
        initStats(specialityStats);

//        getStatsBadPerformance(specialityStats);
        getStatsGoodPerformance(specialityStats, vets);

        return specialityStats;
    }

    private void getStatsBadPerformance(Map<String, Integer> specialityStats) {
//        adding a thread sleep
        try {
            Thread.sleep(2000);
        }
        catch(Exception e) {
        }
        // This is unoptimized code, which gets ids of all Vets in DB
        // and then queries for each record one by one
        List<Integer> vetIds = this.vets.getAllIds();

        vetIds.forEach(vetId -> {
            Vet vetById = this.vets.getVetById(vetId); // Querying DB for each veterinary by ID - excessive IO
            countStats(specialityStats, vetById);
        });
    }
////
    private void getStatsGoodPerformance(Map<String, Integer> specialityStats, Vets vets) {
        // This is optimized version. We already have collection of all veterinarians so we will work with it instead
        // of querying database again

        vets.getVetList().forEach(vet -> countStats(specialityStats, vet));
    }

    private void initStats(Map<String, Integer> specialityStats) {
        specialityStats.put("radiology", 0);
        specialityStats.put("surgery", 0);
        specialityStats.put("dentistry", 0);
    }

    private void countStats(Map<String, Integer> specialityStats, Vet vetById) {
        List<Specialty> specialties = vetById.getSpecialties();
        specialties.forEach(specialty -> {
            switch (specialty.toString()) {
                case RADIOLOGY:
                    specialityStats.put(RADIOLOGY, specialityStats.get(RADIOLOGY) + 1);
                    break;
                case SURGERY:
                    specialityStats.put(SURGERY, specialityStats.get(SURGERY) + 1);
                    break;
                case DENTISTRY:
                    specialityStats.put(DENTISTRY, specialityStats.get(DENTISTRY) + 1);
                    break;
            }
        });
    }

    @GetMapping({ "/vets" })
    public @ResponseBody Vets showResourcesVetList() {
        // Here we are returning an object of type 'Vets' rather than a collection of Vet
        // objects so it is simpler for JSon/Object mapping
        Vets vets = new Vets();
        vets.getVetList().addAll(this.vets.findAll());
        return vets;
    }

}
