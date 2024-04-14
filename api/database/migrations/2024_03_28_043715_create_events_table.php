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
            $table->dateTime("startTime");
            $table->dateTime("endTime");
            $table->unsignedBigInteger('calendarId');
            $table->string("timezone");
            $table->string("color")->nullable();
            $table->string("location")->nullable();
            $table->boolean("isAllDay")->default(false);
            $table->boolean("isPrivate")->default(false);
            $table->json("participants")->nullable();

            $table->foreign('calendarId')->references('id')->on('calendars');
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
