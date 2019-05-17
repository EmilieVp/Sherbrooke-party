class Party {
  constructor (idHost, type, title, description, price, max_guest, schedule, latitude, longitude) {
    this.idHost = idHost;
        this.type = type;
        this.title = title;
        this.description = description;
        this.price = price;
        this.max_guest = max_guest;
        this.schedule = schedule;
        this.latitude = latitude;
        this.longitude = longitude;
		this.idUsersGuest = [];
  }

  toJSON () {
    return {
      'idHost': this.idHost,
      'type': this.type,
      'title': this.title,
      'description': this.description,
      'price': this.price,
      'max_guest': this.max_guest,
      'schedule': this.schedule,
      'latitude': this.latitude,
      'longitude': this.longitude,
      'idUsersGuest': this.idUsersGuest
    };
  }
}
module.exports = Party;