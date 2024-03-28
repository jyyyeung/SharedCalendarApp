<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Event extends Model
{
    use HasFactory;
    protected $fillable = ['title', 'description', 'start_time', 'end_time', 'calendar_id', 'timezone', 'location', 'color', 'is_all_day', 'is_private', 'participants'];
}
