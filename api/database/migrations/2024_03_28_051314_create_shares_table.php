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
        Schema::create('shares', function (Blueprint $table) {
            $table->id();
            $table->timestamps();
            $table->unsignedBigInteger('calendarId');
            $table->unsignedBigInteger('accountId');
            $table->string('permission')->default('READ');

            $table->foreign('calendarId')->references('id')->on('calendars');
            $table->foreign('accountId')->references('id')->on('accounts');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('shares');
    }
};
