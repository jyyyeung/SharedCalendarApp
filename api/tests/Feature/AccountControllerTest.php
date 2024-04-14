<?php

use App\Models\Account;

uses(\Illuminate\Foundation\Testing\RefreshDatabase::class);

it('account model exists', function () {
    $account = Account::factory()->create();
    $this->assertModelExists($account);
});


it('account can be instantiated', function () {
    $this->assertDatabaseCount('accounts', 0);
    $account = Account::factory()->create();
    $this->assertDatabaseCount('accounts', 1);
});

it('multiple accounts can be instantiated', function () {
    $account = Account::factory()->count(3)->create();
    $this->assertDatabaseCount('accounts', 3);
});
