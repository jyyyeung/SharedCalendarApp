<?php

use App\Models\Event;

uses(\Illuminate\Foundation\Testing\RefreshDatabase::class);

it('Event model exists', function () {
    $event = Event::factory()->create();
    $this->assertModelExists($event);
});


it('Event can be instantiated', function () {
    $this->assertDatabaseCount('events', 0);
    $event = Event::factory()->create();
    $this->assertDatabaseCount('events', 1);
});

it('multiple events can be instantiated', function () {
    $Event = Event::factory()->count(3)->create();
    $this->assertDatabaseCount('events', 3);
});
