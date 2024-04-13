<?php

namespace App\Exceptions;

use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Foundation\Exceptions\Handler as ExceptionHandler;
use Throwable;

class Handler extends ExceptionHandler
{

    /**
     * Register the exception handling callbacks for the application.
     */
    public function register(): void
    {
        // This will replace our 404 response with
        // a JSON response.
        $this->renderable(function (ModelNotFoundException $e, $request) {
            return response()->json([
                'error' => 'Resource not found'
            ], 404);
        });


        $this->reportable(function (Throwable $e) {
            //
        });
    }
}
