<?php

namespace Database\Factories;

use App\Models\Account;
use App\Models\Calendar;

use Illuminate\Database\Eloquent\Factories\Factory;

class CalendarFactory extends Factory
{
    protected $model = Calendar::class;

    public function definition()
    {

        return [
            'color' => $this->faker->hexColor(),
            'name' => $this->faker->word(),
            'timezone' => $this->faker->timezone(),
            'ownerId' => Account::factory()
        ];
    }
}
