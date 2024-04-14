<?php

namespace Database\Factories;

use App\Models\Calendar;
use App\Models\Event;
use Illuminate\Database\Eloquent\Factories\Factory;

class EventFactory extends Factory
{
    protected $model = Event::class;

    /**
     * Define the model's default state.
     *
     * @return array
     */
    public function definition()
    {
        return [
            'title' => $this->faker->sentence(),
            'description' => $this->faker->paragraph(),
            'startTime' => $this->faker->dateTime(),
            'endTime' => $this->faker->dateTime(),
            'calendarId' => Calendar::factory(),
            'timezone' => $this->faker->timezone(),
            'location' => $this->faker->address(),
            'color' => $this->faker->hexColor(),
            // 'isAllDay' => $this->faker->boolean(),
            // 'isPrivate' => $this->faker->boolean(),
            // 'participants' =>
            // json_encode($this->faker->words(3)),
        ];
    }
}
