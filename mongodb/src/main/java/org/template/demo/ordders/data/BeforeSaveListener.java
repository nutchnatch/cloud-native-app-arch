package org.template.demo.ordders.data;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

@Component
public class BeforeSaveListener extends AbstractMongoEventListener<BaseEntity> {

    @Override
    public void onBeforeSave(BeforeSaveEvent<BaseEntity> event) {
        DateTime timeStamp = new DateTime();
        if(event.getSource().getCreatedAt() == null) {
            event.getSource().setCreatedAt(timeStamp);
        }

        event.getSource().setLastModified(timeStamp);
        super.onBeforeSave(event);
    }
}
