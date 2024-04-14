<?php

namespace Database\Seeders;

use App\Models\User;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;

class UsersTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        if (env("APP_ENV") === "production") {
            exit("This seeder cannot be run in production mode.");
        } else {
            // Let's truncate our existing records to start from scratch.
            DB::statement('SET FOREIGN_KEY_CHECKS=0;');
            User::truncate();
            DB::statement('SET FOREIGN_KEY_CHECKS=1;');

            $password = Hash::make("password");
            for ($i = 0; $i < 1; $i++) {
                $users = [
                    'name' => 'admin',
                    'email' => 'test1@example.com',
                    'password' => $password,
                    'created_at' => now(),
                    'updated_at' => now()
                ];
            }
            User::insert($users);
        }
    }
}
