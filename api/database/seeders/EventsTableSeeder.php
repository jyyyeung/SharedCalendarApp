<?php

namespace Database\Seeders;

use App\Models\Calendar;
use App\Models\Event;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class EventsTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        // Let's truncate our existing records to start from scratch.
        DB::statement('SET FOREIGN_KEY_CHECKS=0;');
        Event::truncate();
        DB::statement('SET FOREIGN_KEY_CHECKS=1;');

        $faker = \Faker\Factory::create();

        // And now, let's create a few events in our database:
        $calendars = Calendar::pluck('id')->toArray();

        for ($i = 0; $i < 50; $i++) {

            Event::create([
                'calendarId' => $faker->randomElement($calendars),
                'title' => $faker->sentence(),
                'description' => $faker->paragraph(),
                'startTime' => $faker->dateTimeBetween('-1 month', '+1 month'),
                'endTime' => $faker->dateTimeBetween('-1 month', '+1 month'),
                'location' => $faker->address(),
                'timezone' => $faker->timezone(),
                'color' => $faker->hexColor(),
                'isAllDay' => $faker->boolean(),
                'isPrivate' => $faker->boolean(),
                'participants' => json_encode($faker->words(3)),
            ]);
        }
    }
}
