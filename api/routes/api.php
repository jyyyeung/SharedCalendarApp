<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\AccountController;
use App\Http\Controllers\Api\CalendarController;
use App\Http\Controllers\Api\EventController;
use App\Http\Controllers\Api\ShareController;
use App\Http\Controllers\Api\UserController;
use App\Models\User;
use Illuminate\Support\Facades\Hash;

Route::post('/login', [AuthController::class, 'login']);
Route::post('/register', [AuthController::class, 'register']);
// Route::get('/user', function (Request $request) {
//     return $request->user();
// })->middleware('auth:sanctum');

Route::middleware(['auth:sanctum'])->group(
    // Requires Authorization Bearer {{ token }} in Headers
    function () { {
            Route::apiResource('users', UserController::class);
            Route::get('/user', [UserController::class, 'me']);
            Route::post('/logout', [AuthController::class, 'logout']);
        }
    }
);

Route::apiResource('accounts', AccountController::class);
Route::apiResource('calendars', CalendarController::class);
Route::apiResource('events', EventController::class);
Route::apiResource('shares', ShareController::class);
