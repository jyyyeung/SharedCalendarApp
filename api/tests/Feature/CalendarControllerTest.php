<?php

use App\Models\Calendar;

use Illuminate\Foundation\Testing\RefreshDatabase;

uses(RefreshDatabase::class);

it('calendar model exists', function () {
    $calendar = Calendar::factory()->create();
    $this->assertModelExists($calendar);
});

it('calendar can be instantiated', function () {
    $this->assertDatabaseCount('calendars', 0);
    $calendar = Calendar::factory()->create();
    $this->assertDatabaseCount('calendars', 1);
});

it('multiple calendars can be instantiated', function () {
    $calendar = Calendar::factory()->count(3)->create();
    $this->assertDatabaseCount('calendars', 3);
});
