function getCurrentYear() {
    const currentDate = new Date();
    const currentYear = currentDate.getFullYear();
    return currentYear;
}

// Example usage
console.log("The current year is: " + getCurrentYear());
