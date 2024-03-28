<?php

namespace Database\Seeders;

use App\Models\Account;
use App\Models\Calendar;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class CalendarsTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        // Let's truncate our existing records to start from scratch.
        DB::statement('SET FOREIGN_KEY_CHECKS=0;');
        Calendar::truncate();
        DB::statement('SET FOREIGN_KEY_CHECKS=1;');

        $faker = \Faker\Factory::create();

        // And now, let's create a few calendars in our database:
        $accounts = Account::pluck('id')->toArray();

        for ($i = 0; $i < 50; $i++) {
            Calendar::create([
                'color' => $faker->hexColor(),
                'name' => $faker->word(),
                'timezone' => $faker->timezone(),
                'owner_id' => $faker->randomElement($accounts)
            ]);
        }
    }
}
