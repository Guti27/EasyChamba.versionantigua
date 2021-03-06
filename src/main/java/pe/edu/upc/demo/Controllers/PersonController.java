 package pe.edu.upc.demo.Controllers;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pe.edu.upc.demo.Entities.Person;
import pe.edu.upc.demo.ServiceInterface.IPersonService;

@Controller
@RequestMapping("/ppersons")
public class PersonController {

	@Autowired
	private IPersonService personService;

	@GetMapping("/new")
	public String newPerson(Model model) {
		model.addAttribute("p", new Person());
		return "persona/frmRegistro";
	}

	@PostMapping("/save")
	public String savePerson(@Valid Person pe, BindingResult binRes, Model model) {
		if (binRes.hasErrors()) {
			return "persona/frmRegistro";
		} else {
			personService.insert(pe);
			model.addAttribute("mensaje", "Se registró correctamente");
			return "redirect:/ppersons/list";
		}
	}

	@GetMapping("/list")
	public String listPerson(Model model) {
		try {
			model.addAttribute("listaPersonas", personService.list());
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}
		return "/persona/frmLista";
	}

	@RequestMapping("/delete")
	public String deletePerson(Map<String, Object> model, @RequestParam(value = "id") Integer id) {
		try {
			if (id != null && id > 0) {
				personService.delete(id);
				model.put("listaPersonas", personService.list());
			}
		} catch (Exception e) {
			model.put("error", e.getMessage());
		}
		return "persona/frmLista";
	}

	// va a llevar el objeto y lo va a mostrar en el formulario

	@RequestMapping("/goupdate/{id}")
	public String goUpdatePerson(@PathVariable int id, Model model) {

		Optional<Person> objPer = personService.listID(id);
		model.addAttribute("pe", objPer.get());
		return "persona/frmActualiza";
	}

	// guardar los cambios
	@PostMapping("/update")
	public String updatePerson(Person p) {
		personService.update(p);
		return "redirect:/ppersons/list";
	}
	
}
