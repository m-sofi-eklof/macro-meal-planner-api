package se.sofiekl.macromealplanner.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import se.sofiekl.macromealplanner.dto.FoodItemRequestDTO;
import se.sofiekl.macromealplanner.model.FoodSource;


public class FoodItemRequestValidator implements ConstraintValidator<FoodItemRequestConstraint, FoodItemRequestDTO> {

    @Override
    public boolean isValid(
            FoodItemRequestDTO value,
            ConstraintValidatorContext context){
        if(value==null || value.source()==null){
            return true; //handles in dto
        }

        boolean valid=true;
        context.disableDefaultConstraintViolation();

        //constraints for manual
        if(value.source()== FoodSource.MANUAL){
            //require name
            if(value.name()==null || value.name().isBlank()){
                context.buildConstraintViolationWithTemplate("FoodSource can not be empty")
                        .addPropertyNode("name")
                        .addConstraintViolation();
                valid=false;
            }

            //require quantityGrams
            if(value.quantityGrams()==null || value.quantityGrams()<=0){
                context.buildConstraintViolationWithTemplate("Quantity grams can not be empty or have negative value")
                        .addPropertyNode("quantityGrams")
                        .addConstraintViolation();
                valid=false;
            }

            //require calorie field
            if(value.calories()==null || value.calories()<=0){
                context.buildConstraintViolationWithTemplate("Calories can not be empty or have negative value")
                        .addPropertyNode("calories")
                        .addConstraintViolation();
                valid=false;
            }

            //require protein field
            if(value.protein()==null || value.protein()<=0){
                context.buildConstraintViolationWithTemplate("Protein can not be empty or have negative value")
                        .addPropertyNode("protein")
                        .addConstraintViolation();
                valid=false;
            }
        }

        // constraints for USDA
        else if(value.source()==FoodSource.USDA){
            //require fdcId
            if(value.fdcId()==null){
                context.buildConstraintViolationWithTemplate("FDC ID can not be empty")
                        .addPropertyNode("fdcId")
                        .addConstraintViolation();
                valid=false;
            }
        }
        return valid;
    }
}
