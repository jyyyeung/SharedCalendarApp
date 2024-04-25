<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\AccountController;
use App\Http\Controllers\Api\AuthController;
use App\Http\Controllers\Api\CalendarController;
use App\Http\Controllers\Api\EventController;
use App\Http\Controllers\Api\ShareController;
use App\Http\Controllers\Api\UserController;
use App\Http\Controllers\Api\CalendarEventController;
use App\Http\Controllers\Api\CalendarShareController;

Route::post('/login', [AuthController::class, 'login']);
Route::post('/register', [AuthController::class, 'register']);

Route::middleware(['auth:sanctum'])->group(
    // Requires Authorization Bearer {{ token }} in Headers
    function () { {
            Route::apiResource('users', UserController::class);
            Route::get('/user', [AuthController::class, 'me']);
            Route::post('/logout', [AuthController::class, 'logout']);

            // Calendar API Routes
            Route::apiResource('calendars', CalendarController::class);
            Route::get('/calendars/me', [CalendarController::class, 'indexOwned']);
            Route::get('/calendars/shared', [CalendarController::class, 'indexShared']);

            Route::apiResource('calendars.shares', CalendarShareController::class)->shallow();
            Route::apiResource('calendars.events', CalendarEventController::class)->shallow();

            Route::apiResource('events', EventController::class)->only(['show', 'update', 'destroy']);
            Route::apiResource('shares', ShareController::class)->only(['show', 'update', 'destroy']);
        }
    }
);
