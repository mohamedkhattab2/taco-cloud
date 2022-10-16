/*
controller that will do the following:
 Handle HTTP GET requests where the request path is /design
 Build a list of ingredients
 Hand off the request and the ingredient data to a view template to be rendered
as HTML and sent to the requesting web browser
 */


package tacos.web;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Taco;
import tacos.TacoOrder;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j  // simple logging faced for java
@Controller  // serves to identify this class as a controller and mark it as a candidate for component scanning, so
// spring will discover it and automatically create an instance from DesignTacoController as a bean in spring application context
@RequestMapping("/design")  // specifies the kind of requests that this controller will handle, in this case DesignTacoController
// handle requests whose path begins of /design
@SessionAttributes("tacoOrder")  // this is indicates that the TacoOrder object that is put in to a model should be maintained in a session
public class DesignTacoController {

    //  Build a list of ingredients
    @ModelAttribute
    public void addIngredientsToModel(Model model){
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("FLTO","Flour Tortilla", Type.WRAP),
                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
                new Ingredient("CHED", "Cheddar", Type.CHEESE),
                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
                new Ingredient("SLSA", "Salsa", Type.SAUCE),
                new Ingredient("SRCR", "Sour Cream", Type.SAUCE)

        );

        Type[] types = Ingredient.Type.values();
        for (Type type:types){
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients,type));
        }



    }
    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        return new TacoOrder();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @GetMapping
    public String showDesignForm() {
        return "design";
    }


    @PostMapping
    public String processTaco(@Valid Taco taco, Errors errors, @ModelAttribute TacoOrder tacoOrder){
        if (errors.hasErrors()){
            return "design";
        }

        tacoOrder.addTaco(taco);
        log.info("Processing taco: {}",taco);
        return "redirect:/orders/current";

    }


    private  Iterable<Ingredient> filterByType
            (List<Ingredient> ingredients, Type type) {

        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());

    }


}
