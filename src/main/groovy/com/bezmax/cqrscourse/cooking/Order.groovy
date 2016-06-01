package com.bezmax.cqrscourse.cooking

import com.google.gson.Gson
import com.google.gson.JsonObject

class Order {
    private JsonObject rootElement

    Order(String data) {
        rootElement = new Gson().fromJson(data, JsonObject.class)
    }

    Order() {
        rootElement = new JsonObject()
        id = UUID.randomUUID()
    }
    
    UUID getId() {
        UUID.fromString(rootElement.get("id").asString)
    }
    
    void setId(UUID uuid) {
        rootElement.remove("id")
        rootElement.addProperty("id", uuid.toString())
    }

    int getTableNumber() {
        rootElement.get("tableNumber").asInt
    }

    void setTableNumber(int table) {
        rootElement.remove("tableNumber")
        rootElement.addProperty("tableNumber", table)
    }

    BigDecimal getTax() {
        rootElement.get("tax").asBigDecimal
    }

    void setTax(BigDecimal tax) {
        rootElement.remove("tax")
        rootElement.addProperty("tax", tax)
    }

    BigDecimal getTotal() {
        rootElement.get("total").asBigDecimal
    }

    void setTotal(BigDecimal total) {
        rootElement.remove("total")
        rootElement.addProperty("total", total)
    }

    boolean isPaid() {
        rootElement.get("paid").asBoolean
    }

    void setPaid(boolean paid) {
        rootElement.remove("paid")
        rootElement.addProperty("paid", paid)
    }

    String getIngredients() {
        rootElement.get("ingredients").asString
    }

    void setIngredients(String ingredients) {
        rootElement.remove("ingredients")
        rootElement.addProperty("ingredients", ingredients)
    }

    List<Item> getItems() {
        rootElement.getAsJsonArray("lineItems").collect {it -> new Item(it.asJsonObject)}
    }

    String serialize() {
        rootElement.toString()
    }

    static class Item {
        private JsonObject rootElement

        Item(JsonObject root) {
            this.rootElement = root
        }

        BigDecimal getPrice() {
            rootElement.get("price").asBigDecimal
        }

        void setPrice(BigDecimal price) {
            rootElement.remove("price")
            rootElement.addProperty("price", price)
        }

        String getItem() {
            rootElement.get("item").asString
        }

        void setItem(String item) {
            rootElement.remove("item")
            rootElement.addProperty("item", item)
        }

        int getQuantity() {
            rootElement.get("quantity").asInt
        }

        void setQuantity(int quantity) {
            rootElement.remove("quantity")
            rootElement.addProperty("quantity", quantity)
        }
    }
}