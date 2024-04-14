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
		Schema::create('calendars', function (Blueprint $table) {
			$table->id();
			$table->timestamps();
			$table->string("color");
			$table->string("name");
			$table->string("timezone")->nullable();
			$table->unsignedBigInteger('ownerId');

			$table->foreign('ownerId')->references('id')->on('accounts');
		});
	}

	/**
	 * Reverse the migrations.
	 */
	public function down(): void
	{
		Schema::dropIfExists('calendars');
	}
};
