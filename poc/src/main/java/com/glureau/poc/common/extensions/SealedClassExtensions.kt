package com.glureau.poc.common.extensions


// Extension function that turns a when statement into an expression
// so the compiler enforces handling of all states.
// https://ryanharter.com/blog/handling-transient-events/
val <T> T.exhaustive: T get() = this
