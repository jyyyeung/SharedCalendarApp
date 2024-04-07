<?php

use App\Models\Share;

uses(\Illuminate\Foundation\Testing\RefreshDatabase::class);

it('share model exists', function () {
    $share = Share::factory()->create();
    $this->assertModelExists($share);
});

it('share can be instantiated', function () {
    $this->assertDatabaseCount('shares', 0);
    $share = Share::factory()->create();
    $this->assertDatabaseCount('shares', 1);
});

it('multiple shares can be instantiated', function () {
    $share = Share::factory()->count(3)->create();
    $this->assertDatabaseCount('shares', 3);
});
