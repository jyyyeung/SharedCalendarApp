<?php

namespace Database\Seeders;

use App\Models\Account;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class AccountsTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {

        // Let's truncate our existing records to start from scratch.
        DB::statement('SET FOREIGN_KEY_CHECKS=0;');
        Account::truncate();
        DB::statement('SET FOREIGN_KEY_CHECKS=1;');


        $faker = \Faker\Factory::create();

        // And now, let's create a few articles in our database:
        for ($i = 0; $i < 50; $i++) {
            Account::create([
                'username' => $faker->username(),
                'email' => $faker->email(),
                'password_hash' => $faker->sha256(),
                'settings' => json_encode([
                    'default_view' => $faker->randomElement(['day', '4-day', 'week', 'month']),
                    'default_calendar' => $faker->randomElement(['work', 'personal', 'school']),
                    'default_timezone' => $faker->timezone(),
                    'default_reminder' => $faker->randomElement(['email', 'popup', 'sms'])
                ])
            ]);
        }
    }
}
