package com.mrtripop.learn.SpringBatchProcessing.processors;

import com.mrtripop.learn.SpringBatchProcessing.models.PeopleEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class PersonItemProcessor implements ItemProcessor<PeopleEntity, PeopleEntity> {

  @Override
  public PeopleEntity process(PeopleEntity people) throws Exception {
    PeopleEntity transformedPeople = PeopleEntity.builder()
        .firstName(people.getFirstName().toUpperCase())
        .lastName(people.getLastName().toUpperCase())
        .build();
    log.info("Converting ({}) into ({})", people, transformedPeople);
    return transformedPeople;
  }
}
