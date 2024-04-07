<?php

test('get calendar', function () {
    $response = $this->get('/api/calendars');

    $response->assertStatus(200);
    // Add more assertions as needed
});

test('get calendar by id', function () {
    $response = $this->get('/api/calendars/1');

    $response->assertStatus(200);
    // Add more assertions as needed
});
