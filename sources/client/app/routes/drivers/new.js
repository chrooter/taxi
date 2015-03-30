import Ember from "ember";

export default Ember.Route.extend({
  model: function(){
    return this.store.createRecord("driver");
  },
  actions: {
    willTransition: function(transition){
      if (this.controller.isDirty() && !confirm("Имеются несохраненные изменения. Вы действительно хотите выйти из редактора?")) {
        transition.abort();
      } else {
        this.controller.discardModel();
        return true;
      }
    }
  }
});
