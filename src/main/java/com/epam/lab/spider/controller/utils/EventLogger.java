package com.epam.lab.spider.controller.utils;

import com.epam.lab.spider.model.entity.Event;
import com.epam.lab.spider.model.entity.impl.BasicEntityFactory;
import com.epam.lab.spider.model.entity.impl.EventImpl;
import com.epam.lab.spider.persistence.service.EventService;
import com.epam.lab.spider.persistence.service.ServiceFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Boyarsky Vitaliy
 */
public class EventLogger {

    private static final ServiceFactory factory = ServiceFactory.getInstance();
    private static final EventService service = factory.create(EventService.class);
    private static Map<Integer, EventLogger> instances = new HashMap<>();
    private static Receiver eventReceiver;
    private static Receiver notificationReceiver;
    private final int userId;

    private EventLogger(int userId) {
        this.userId = userId;
    }

    public static synchronized EventLogger getLogger(int userId) {
        if (!instances.containsKey(userId)) {
            instances.put(userId, new EventLogger(userId));
        }
        return instances.get(userId);
    }

    public boolean info(String title, String messages) {
        return createEvent(EventImpl.Type.INFO, title, messages);
    }

    public boolean success(String title, String messages) {
        return createEvent(EventImpl.Type.SUCCESS, title, messages);
    }

    public boolean warn(String title, String messages) {
        return createEvent(EventImpl.Type.WARN, title, messages);
    }

    public boolean error(String title, String messages) {
        return createEvent(EventImpl.Type.ERROR, title, messages);
    }

    private boolean createEvent(EventImpl.Type type, String title, String message) {
        Event event = BasicEntityFactory.getSynchronized().createEvent();
        event.setType(type);
        event.setUserId(userId);
        event.setTitle(title);
        event.setMessage(message);
        event.setTime(new Date());
        if (sendEvent(event))
            event.setShown(true);
        else
            sendNotification(event);
        return service.insert(event);
    }

    private boolean sendEvent(Event event) {
        return eventReceiver != null && eventReceiver.send(userId, Notification.basic(event).toString());
    }

    private boolean sendNotification(Event event) {
        return notificationReceiver != null && notificationReceiver.send(userId, "notf|" + event.getType().toString()
                + "|" + event.getTitle() + "|" + new SimpleDateFormat("HH:mm").format(event.getTime()));
    }

    public static class EventSender implements Sender {

        @Override
        public void accept(Receiver receiver) {
            eventReceiver = receiver;
        }

        @Override
        public void history(int clientId) {
            List<Event> events = service.getByShownUserId(clientId);
            Map<EventImpl.Type, List<Event>> eventGroupByType = new HashMap<>();
            for (Event event : events) {
                List list;
                if (eventGroupByType.containsKey(event.getType()))
                    list = eventGroupByType.get(event.getType());
                else
                    list = new ArrayList();
                list.add(event);
                eventGroupByType.put(event.getType(), list);
            }
            for (EventImpl.Type eventType : eventGroupByType.keySet()) {
                List<Event> allEvent = eventGroupByType.get(eventType);
                List<Event> list = new ArrayList<>();
                for (Event event : allEvent) {
                    if (list.size() == 5) {
                        eventReceiver.send(clientId, Notification.list(list).toString());
                        list = new ArrayList<>();
                    }
                    list.add(event);
                }
                if (list.size() > 0)
                    eventReceiver.send(clientId, Notification.list(list).toString());
            }
            service.markAsShowByUserId(clientId);
        }

    }

    public static class NotificationSender implements Sender {

        @Override
        public void accept(Receiver receiver) {
            notificationReceiver = receiver;
        }

        @Override
        public void history(int clientId) {

        }

    }

}