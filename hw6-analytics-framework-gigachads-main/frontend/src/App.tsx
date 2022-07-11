import { Component } from 'react'
import './App.css'
import { Square } from './Square'

let oldHref = 'http://localhost:3000'


interface Cells {
  instructions: String
  images: String[]
  username: String
  screen: number
}

interface Props {
}

class App extends Component<Props, Cells> {
  constructor (props: Props) {
    super(props)
    this.state = {
      instructions: '',
      images: [],
      username: '',
      screen: 0
    }
    this.clickHandlerUsername = this.clickHandlerUsername.bind(this)
  }

  // handler when user inputs specific account username
  clickHandlerUsername (username: String) {
    this.setState({ username: username })
  }

  updateInstructions () {
    let instructions = 'TODO'
    this.setState({ instructions: instructions })
  }


  getImages (p: any): String[] {
    return p.filenames
  }

  // function that is called when user selects the data plugin
  async dataPlugin (url: String) {
    const href = 'dataplugin?' + url.split('?')[1]
    const response = await fetch(href)
    console.log("got response: " + response)
    const json = await response.json()
    console.log("Json: " + json)
    this.setState({ screen: 1})
  }

  // function that is called when user selects the visualization plugin
  async visPlugin (url: String) {
    const href = 'visplugin?' + url.split('?')[1]
    const response = await fetch(href)
    console.log("got response: " + response)
    const json = await response.json()
    console.log("Json: " + json)
    this.setState({ screen: 2, images: this.getImages(json)})
    console.log(this.getImages(json)[0]);
  }

  async switch () {
    if (
      window.location.href.split('?')[0] === 'http://localhost:3000/dataplugin' &&
      oldHref !== window.location.href
    ) {
      console.log('dataplugin')
      this.dataPlugin(window.location.href)
      oldHref = window.location.href
    } else if (
      window.location.href.split('?')[0] === 'http://localhost:3000/visplugin' &&
      oldHref !== window.location.href
    ) {
      console.log('dataplugin')
      this.visPlugin(window.location.href)
      oldHref = window.location.href
    }
  };

  generateDataPluginScreen () {
    let instagramButton = <a href={'/dataplugin?name=instagram&username=' + this.state.username}><button>Instagram</button></a>;
    let twitterButton = <a href={'/dataplugin?name=twitter&username=' + this.state.username}><button>Twitter</button></a>;
    let redditButton = <a href={'/dataplugin?name=reddit&username=' + this.state.username}><button>Reddit</button></a>;
    return (
      <div className='App'>
        <div style={{ textAlign: 'center' }}>
          <h1>Chad Framework</h1>
          <p>Instruction: Choose a data plugin.</p>
          <br />
        </div>
        <h3>Enter Username: </h3>
        <input 
        type="text" 
        name="topicBox" 
        placeholder="Username" 
        value={ this.state.username.valueOf() }
        onChange={e => this.clickHandlerUsername(e.target.value) } 
        style={{width: "370px", height: "75px"}}
      />
        <div id='bottombar'>
          {instagramButton}
          {twitterButton}
          {redditButton}
        </div>
      </div>
    )
  }

  generateVisPluginScreen () {
    let colorGradient = <a href={'/visplugin?name=colorgradient'}><button>Color Gradient</button></a>;
    let colorLikes = <a href={'/visplugin?name=colorandtime'}><button>Color vs. Time</button></a>;
    let colorTime = <a href={'/visplugin?name=colorandlikes'}><button>Color vs. Likes</button></a>;
    return (
      <div className='App'>
        <div style={{ textAlign: 'center' }}>
          <h1>Chad Framework</h1>
          <p>Instruction: Choose a visualization plugin.</p>
          <br />
        </div>
        <div id='bottombar'>
          {colorGradient}
          {colorLikes}
          {colorTime}
        </div>
      </div>
    )
  }

  generateOutputScreen () {
    console.log(this.state.images.length);
    console.log("./visualization_images/" + this.state.images[0].valueOf())
    let images = <img src={require("./visualization_images/" + this.state.images[0].valueOf())} height="300" width="300"></img>
    // console.log("./visualization_images/" + this.state.images[0].valueOf())
    if(!window.location.hash) {
      window.location.href = window.location + '#loaded';
      window.location.reload();
  }
    return (
      <div className='App'>
        <div style={{ textAlign: 'center' }}>
          <h1>Chad Framework</h1>
          <p>View Your Images!</p>
          <br />
        </div>
        <div id='bottombar'>
          {images}
        </div>
      </div>
    )
  }

  render () {
    this.switch()
    if (this.state.screen == 0) {
      return this.generateDataPluginScreen()
    } else if (this.state.screen == 1) {
      return this.generateVisPluginScreen()
    }
    return this.generateOutputScreen()
  };
};

export default App
