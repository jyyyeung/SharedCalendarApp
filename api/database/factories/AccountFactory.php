<?php

namespace Database\Factories;

use App\Models\Account;
use Illuminate\Database\Eloquent\Factories\Factory;

class AccountFactory extends Factory
{
    protected $model = Account::class;

    public function definition()
    {
        return [
            'username' => $this->faker->username(),
            'email' => $this->faker->email(),
            'passwordHash' => $this->faker->sha256(),
            'settings' => json_encode([
                'default_view' => $this->faker->randomElement(['day', '4-day', 'week', 'month']),
                'default_calendar' => $this->faker->randomElement(['work', 'personal', 'school']),
                'default_timezone' => $this->faker->timezone(),
                'default_reminder' => $this->faker->randomElement(['email', 'popup', 'sms'])
            ])
        ];
    }
}
