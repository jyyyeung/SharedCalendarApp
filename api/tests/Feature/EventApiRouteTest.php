<?php


use App\Models\Event;

uses(\Illuminate\Foundation\Testing\RefreshDatabase::class);


test('get events', function () {
    $response = $this->get('/api/events');

    $response->assertStatus(200);
});
test('get event by id', function () {
    $event = Event::factory()->create();

    $response = $this->get('/api/events/' . $event->id);

    $response->assertStatus(200);
});
test('create event', function () {
    $event = Event::factory()->make()->toArray();

    $response = $this->post('/api/events', $event);

    $response->assertStatus(201);
});

test('delete event', function () {
    $event = Event::factory()->create();

    $response = $this->delete('/api/events/' . $event->id);

    $response->assertStatus(204);
});

test('update event', function () {
    $event = Event::factory()->create();
    $newEvent = Event::factory()->make()->toArray();

    $response = $this->put('/api/events/' . $event->id, $newEvent);

    $response->assertStatus(200);
});
