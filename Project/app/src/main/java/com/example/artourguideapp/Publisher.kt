package com.example.artourguideapp

interface Publisher {
    var subscribers : ArrayList<Subscriber>

    fun register (subscriber: Subscriber) : Subscriber {
        subscribers.add(subscriber)
        return subscriber
    }

    fun remove (subscriber: Subscriber) : Subscriber {
        subscribers.remove(subscriber)
        return subscriber
    }

    fun updateSubscribers() {
        for (sub in subscribers) {
            sub.update()
        }
    }
}