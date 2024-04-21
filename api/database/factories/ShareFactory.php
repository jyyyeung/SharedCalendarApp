<?php

namespace Database\Factories;

use App\Models\Account;
use App\Models\Calendar;
use App\Models\Share;
use Illuminate\Database\Eloquent\Factories\Factory;

class ShareFactory extends Factory
{
    protected $model = Share::class;

    public function definition()
    {
        return [
            'calendarId' => Calendar::factory(),
            'userId' => Account::factory(),
            'permission' => $this->faker->randomElement(["READ", "WRITE", "AVAILABILITY", "ADMIN"])
        ];
    }
}
