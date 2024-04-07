<?php

namespace Database\Seeders;

use App\Models\Account;
use App\Models\Calendar;
use App\Models\Share;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class SharesTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        // Let's truncate our existing records to start from scratch.
        DB::statement('SET FOREIGN_KEY_CHECKS=0;');
        Share::truncate();
        DB::statement('SET FOREIGN_KEY_CHECKS=1;');

        $faker = \Faker\Factory::create();

        // And now, let's create a few shares in our database:
        $calendars = Calendar::pluck('id')->toArray();
        $accounts = Account::pluck('id')->toArray();

        for ($i = 0; $i < 50; $i++) {

            Share::create([
                'calendarId' => $faker->randomElement($calendars),
                'accountId' => $faker->randomElement($accounts),
                'permission' => $faker->randomElement(['read', 'write', 'share', 'invite']),
                // 'can_edit' => $faker->boolean(),
                // 'can_share' => $faker->boolean(),
                // 'can_invite' => $faker->boolean(),
            ]);
        }
    }
}
