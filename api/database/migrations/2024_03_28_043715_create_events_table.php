<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('events', function (Blueprint $table) {
            $table->id();
            $table->timestamps();
            $table->string("title");
            $table->text("description")->nullable();
            $table->dateTime("start_time");
            $table->dateTime("end_time");
            $table->unsignedBigInteger('calendar_id');
            $table->string("timezone");
            $table->string("color")->nullable();
            $table->string("location")->nullable();
            $table->boolean("is_all_day")->default(false);
            $table->boolean("is_private")->default(false);
            $table->json("participants")->nullable();

            $table->foreign('calendar_id')->references('id')->on('calendars');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('events');
    }
};
