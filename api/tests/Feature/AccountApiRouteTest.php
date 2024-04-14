<?php

use App\Models\Account;

uses(\Illuminate\Foundation\Testing\RefreshDatabase::class);

test('get accounts', function () {
    $response = $this->get('/api/accounts');

    $response->assertStatus(200);
});

test('get account by id', function () {
    $account = Account::factory()->create();

    $response = $this->get('/api/accounts/' . $account->id);

    $response->assertStatus(200);
});

test('create account', function () {
    $account = Account::factory()->make()->toArray();

    $response = $this->post('/api/accounts', $account);

    $response->assertStatus(201);
});

test('delete account', function () {
    $account = Account::factory()->create();

    $response = $this->delete('/api/accounts/' . $account->id);

    $response->assertStatus(204);
});

test('update account', function () {
    $account = Account::factory()->create();
    $newAccount = Account::factory()->make()->toArray();

    $response = $this->put('/api/accounts/' . $account->id, $newAccount);

    $response->assertStatus(200);
});
