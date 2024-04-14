<?php

use App\Models\Calendar;

uses(\Illuminate\Foundation\Testing\RefreshDatabase::class);

test('get calendars', function () {
    $response = $this->get('/api/calendars');

    $response->assertStatus(200);
    // Add more assertions as needed
});
test('get calendar by id', function () {
    $calendar = Calendar::factory()->create();

    $response = $this->get('/api/calendars/' . $calendar->id);

    $response->assertStatus(200);
    // Add more assertions as needed
});

test('create calendar', function () {
    $calendar = Calendar::factory()->make()->toArray();

    $response = $this->post('/api/calendars', $calendar);

    $response->assertStatus(201);
    // Add more assertions as needed
});

test('delete calendar', function () {
    $calendar = Calendar::factory()->create();

    $response = $this->delete('/api/calendars/' . $calendar->id);

    $response->assertStatus(204);
});

test('update calendar', function () {
    $calendar = Calendar::factory()->create();
    $newCalendar = Calendar::factory()->make()->toArray();

    $response = $this->put('/api/calendars/' . $calendar->id, $newCalendar);

    $response->assertStatus(200);
});
