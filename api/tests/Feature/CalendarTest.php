<?php

it('has calendar page', function () {
    $response = $this->get('/calendars');

    $response->assertStatus(200);
});
