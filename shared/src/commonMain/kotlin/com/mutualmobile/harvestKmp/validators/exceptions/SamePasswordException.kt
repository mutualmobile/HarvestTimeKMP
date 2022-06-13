package com.mutualmobile.harvestKmp.validators.exceptions

class SamePasswordException : Exception(
    "Old and New passwords cannot be the same. Please enter another new password!"
)