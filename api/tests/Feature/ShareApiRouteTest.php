<?php

use App\Models\Share;


uses(\Illuminate\Foundation\Testing\RefreshDatabase::class);


test('get shares', function () {
    $response = $this->get('/api/shares');

    $response->assertStatus(200);
});

test('get share by id', function () {
    $share = Share::factory()->create();

    $response = $this->get('/api/shares/' . $share->id);

    $response->assertStatus(200);
});

test('create share', function () {
    $share = Share::factory()->make()->toArray();

    $response = $this->post('/api/shares', $share);

    $response->assertStatus(201);
});

test('delete share', function () {
    $share = Share::factory()->create();

    $response = $this->delete('/api/shares/' . $share->id);

    $response->assertStatus(204);
});

test('update share', function () {
    $share = Share::factory()->create();
    $newShare = Share::factory()->make()->toArray();

    $response = $this->put('/api/shares/' . $share->id, $newShare);

    $response->assertStatus(200);
});
